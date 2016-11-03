package com.nm.base.app;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.nm.base.crash.GlobalCrashHandler;
import com.nm.base.util.AppUtils;
import com.nm.base.util.FileUtils;
import com.nm.base.util.ToastWrapper;

/**
 * Created by huangming on 2016/9/25.
 */

public class BaseApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        ToastWrapper.init(this);
        GlobalCrashHandler.INSTANCE.init();
        if (AppUtils.isMainProcess(this)) {
            // fresco初始化
            Fresco.initialize(this, buildFrescoConfig());

            onCreateOnMainThread();
        }
    }
    
    protected void onCreateOnMainThread() {
        
    }
    
    private ImagePipelineConfig buildFrescoConfig() {
        
        final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
        final int MAX_MEM_CACHE_SIZE = MAX_HEAP_SIZE >> 2;
        final int MAX_DISK_CACHE_SIZE = MAX_MEM_CACHE_SIZE >> 2;
        
        // 内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEM_CACHE_SIZE, // 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE, // 内存缓存中图片的最大数量。
                MAX_MEM_CACHE_SIZE, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE); // 内存缓存中单个图片的最大大小。
        
        // 修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        
        // 小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder()
                .setBaseDirectoryPath(getCacheDir())// 缓存图片基路径
                .setBaseDirectoryName("cache_small")// 文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)// 默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_SIZE >> 1)// 缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_SIZE >> 2)// 缓存的最大大小,当设备极低磁盘空间
                // .setVersion(version)
                .build();
        
        // 默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder()
                .setBaseDirectoryPath(FileUtils.getFileCacheDir(this, "fresco"))// 缓存图片基路径
                .setBaseDirectoryName("cache_normal")// 文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)// 默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_SIZE >> 1)// 缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_SIZE >> 2)// 缓存的最大大小,当设备极低磁盘空间
                .build();
        
        // 缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(this)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)// 内存缓存配置（一级缓存，已解码的图片）
                .setMainDiskCacheConfig(diskCacheConfig)// 磁盘缓存配置（总，三级缓存）
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig);// 磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
        
        return configBuilder.build();
    }
}
