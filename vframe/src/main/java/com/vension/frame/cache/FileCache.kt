package com.vension.frame.cache

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat

/**
 * ===================================================================
 * @author: Created by Vension on 2019/2/14 15:40.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/2/14 15:40
 * @desc:   缓存路径处理类
 * ===================================================================
 */
object FileCache {

    private val F_IMAGE = "f_image"
    private val F_VIDEO = "f_video"
    private val F_AUDIO = "f_audio"
    private val F_FILE = "f_file"

    private lateinit var _Path: String
    private var _CacheImage: File? = null
    private var _CacheVideo: File? = null
    private var _CacheAudio: File? = null
    private var _CacheFile: File? = null

    val TYPE_B = 1// 获取文件大小单位为B的double值
    val TYPE_KB = 2// 获取文件大小单位为KB的double值
    val TYPE_MB = 3// 获取文件大小单位为MB的double值
    val TYPE_GB = 4// 获取文件大小单位为GB的double值


    fun init(context: Context) {
        _Path = getLocatDir(context) + File.separator

        if (_CacheImage == null) {
            _CacheImage = File(_Path + F_IMAGE)
            if (!_CacheImage!!.mkdir()) {
                _CacheImage!!.mkdirs()
            }
        }

        if (_CacheVideo == null) {
            _CacheVideo = File(_Path + F_VIDEO)
            if (!_CacheVideo!!.mkdir()) {
                _CacheVideo!!.mkdirs()
            }
        }

        if (_CacheAudio == null) {
            _CacheAudio = File(_Path + F_AUDIO)
            if (!_CacheAudio!!.mkdir()) {
                _CacheAudio!!.mkdirs()
            }
        }

        if (_CacheFile == null) {
            _CacheFile = File(_Path + F_FILE)
            if (!_CacheFile!!.mkdir()) {
                _CacheFile!!.mkdirs()
            }
        }
    }


    fun isSdcardExist(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun getCachePath(): String {
        return _Path
    }

    fun getCacheImagePath(): String {
        return getPath(_CacheImage)
    }

    fun getCacheVideoPath(): String {
        return getPath(_CacheVideo)
    }

    fun getCacheAudioPath(): String {
        return getPath(_CacheAudio)
    }

    fun getCacheFilePath(): String {
        return getPath(_CacheFile)
    }


    private fun getPath(file: File?): String {
        if (file != null) {
            if (!file.exists() && !file.mkdir()) {
                file.mkdirs()
            }
            return file.absolutePath
        }
        return "/Android/data/com.r747223875.nfu/cache/errCache/"
    }


    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     */
    fun getFileOrFilesSize(filePath: String, sizeType: Int): Double {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            if (file.isDirectory) {
                blockSize = getFileSizes(file)
            } else {
                blockSize = getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formetFileSize(blockSize, sizeType)
    }

    /**
     * 自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件或指定文件夹路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    fun getAutoFileOrFilesSize(filePath: String): String {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            if (file.isDirectory) {
                blockSize = getFileSizes(file)
            } else {
                blockSize = getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formetFileSize(blockSize)
    }

    // 文件目录下文件随APK卸掉而移除
    fun getLocatDir(context: Context): String {
        var mCacheFile: File? = null

        try {
            if (isSdcardExist()) {
                mCacheFile = context.externalCacheDir
            }

            if (mCacheFile == null) {
                mCacheFile = context.cacheDir
            }
        } catch (e: Exception) {
            mCacheFile = context.filesDir
        }

        if (!mCacheFile!!.exists()) {
            mCacheFile.mkdirs()
        }
        return mCacheFile.absolutePath
    }

    /**
     * 获取指定文件大小
     */
    @Throws(Exception::class)
    fun getFileSize(file: File?): Long {
        var size: Long = 0
        if (file != null && file.exists()) {
            var mFileInputStream = FileInputStream(file)
            size = mFileInputStream.available().toLong()
            mFileInputStream.close()
        } else {
            file!!.createNewFile()
        }
        return size
    }

    /**
     * 获取指定文件夹大小
     */
    @Throws(Exception::class)
    fun getFileSizes(file: File?): Long {
        if (file == null || !file.isDirectory || !file.exists()) {
            return 0L
        }
        var size: Long = 0
        val flist = file.listFiles() ?: return 0L
        for (i in flist.indices) {
            if (flist[i].isDirectory) {
                size = size + getFileSizes(flist[i])
            } else {
                size = size + getFileSize(flist[i])
            }
        }
        return size
    }

    /**
     * 转换文件大小
     */
    fun formetFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 转换文件大小,指定转换的类型
     */
    fun formetFileSize(fileS: Long, sizeType: Int): Double {
        val df = DecimalFormat("#.00")
        var fileSizeLong = 0.0
        when (sizeType) {
            TYPE_B -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble()))
            TYPE_KB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))
            TYPE_MB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))
            TYPE_GB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))
        }
        return fileSizeLong
    }


    // 按目录删除文件夹文件方法
    private fun deleteFolderFile(filePath: String, deleteThisPath: Boolean): Boolean {
        try {
            val file = File(filePath)
            if (file.isDirectory) {
                val files = file.listFiles()
                for (file1 in files!!) {
                    deleteFolderFile(file1.absolutePath, true)
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory) {
                    file.delete()
                } else {
                    if (file.listFiles()!!.size == 0) {
                        file.delete()
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun clear(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }

        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles.isNullOrEmpty()) {
                return
            }

            for (i in childFiles.indices) {
                clear(childFiles[i])
            }
        }
    }


//	static public Disposable getCacheSize(Consumer<String> consumer) {
//		return Observable.just(getCachePath())
//				.compose(new ObservableTransformer<String, String>() {
//					@Override
//					public ObservableSource<String> apply(Observable<String> upstream) {
//						return upstream.map(new Function<String, String>() {
//							@Override
//							public String apply(String path) throws Exception {
//								return getAutoFileOrFilesSize(path);
//							}
//						});
//					}
//				})
//				.compose(FileCache.<String>rxSchedulerHelper())
//				.subscribe(consumer);
//	}
//
//	static public Disposable clearCache(final Consumer<Boolean> consumer) {
//		return Observable.just(getCachePath())
//				.compose(new ObservableTransformer<String, Boolean>() {
//					@Override
//					public ObservableSource<Boolean> apply(Observable<String> upstream) {
//						return upstream.map(new Function<String, Boolean>() {
//							@Override
//							public Boolean apply(String path) throws Exception {
//								ImageLoader.glide(getApplication()).clearDiskCache();
//								return deleteFolderFile(path, false);
//							}
//						});
//					}
//				})
//				.compose(FileCache.<Boolean>rxSchedulerHelper())
//				.subscribe(new Consumer<Boolean>() {
//
//					@Override
//					public void accept(Boolean result) throws Exception {
//						ImageLoader.glide(getApplication()).clearMemory();
//						if (consumer != null) {
//							consumer.accept(result);
//						}
//					}
//				});
//	}
//
//
//
//
//	/**
//	 * 统一线程处理
//	 * AndroidSchedulers.mainThread() 主线程
//	 * Schedulers.immediate() 当前线程，即默认Scheduler
//	 * Schedulers.newThread() 启用新线程
//	 * Schedulers.io() IO线程，内部是一个数量无上限的线程池，可以进行文件、数据库和网络操作。
//	 * Schedulers.computation() CPU计算用的线程，内部是一个数目固定为CPU核数的线程池，适合于CPU密集型计算，不能操作文件、数据库和网络。
//	 */
//	static private <T> ObservableTransformer<T, T> rxSchedulerHelper() {
//		return new ObservableTransformer<T, T>() {
//
//			@Override
//			public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
//				return upstream.subscribeOn(Schedulers.io())
//						.unsubscribeOn(Schedulers.io())
//						.observeOn(AndroidSchedulers.mainThread());
//			}
//		};
//	}

}