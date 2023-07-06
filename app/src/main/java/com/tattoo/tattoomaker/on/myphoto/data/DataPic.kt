package com.tattoo.tattoomaker.on.myphoto.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.model.picture.BucketPicModel
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import java.io.File
import java.io.IOException

object DataPic {

    private val EXTERNAL_COLUMNS_PIC = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        "\"" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "\""
    )

    private val EXTERNAL_COLUMNS_PIC_API_Q = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA
    )

    fun getBucketPictureList(context: Context) {

        val lstBucket = ArrayList<BucketPicModel>()
        var lstPic: ArrayList<PicModel>
        val lstAll = ArrayList<PicModel>()
        val CONTENT_URI: Uri
        val COLUMNS: Array<String>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q
        } else {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            COLUMNS = EXTERNAL_COLUMNS_PIC
        }

        context.contentResolver.query(
            CONTENT_URI,
            COLUMNS,
            null,
            null,
            MediaStore.Images.Media.DEFAULT_SORT_ORDER
        ).use { cursor ->

            val idColumn = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {

                val id = cursor.getString(idColumn)
                val bucket = cursor.getString(bucketColumn)
                val data = cursor.getString(dataColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                val size = UtilsBitmap.getImageSize(context, Uri.parse(contentUri.toString()))
                val ratio = size[0] / size[1]
                val file = File(data)
                if (file.canRead()) {
                    lstAll.add(PicModel(id, bucket, ratio, contentUri.toString(), false))
                    var check = false
                    if (lstBucket.isEmpty()) {
                        lstPic = ArrayList<PicModel>()
                        if (bucket != null) {
                            lstBucket.add(BucketPicModel(lstPic, bucket))
                            lstPic.add(PicModel(id, bucket, ratio, contentUri.toString(), false))
                        } else {
                            lstBucket.add(BucketPicModel(lstPic, ""))
                            lstPic.add(PicModel(id, "", ratio, contentUri.toString(), false))
                        }
                    } else {
                        for (i in lstBucket.indices) {
                            if (bucket == null) {
                                lstBucket[i].lstPic.add(PicModel(id, "", ratio, contentUri.toString(), false))
                                check = true
                                break
                            }
                            if (bucket == lstBucket[i].bucket) {
                                lstBucket[i].lstPic.add(PicModel(id, bucket, ratio, contentUri.toString(), false))
                                check = true
                                break
                            }
                        }
                        if (!check) {
                            lstPic = ArrayList()
                            lstPic.add(PicModel(id, bucket, ratio, contentUri.toString(), false))
                            lstBucket.add(BucketPicModel(lstPic, bucket!!))
                        }
                    }
                }
            }
            lstBucket.add(0, BucketPicModel(lstAll, context.getString(R.string.all)))
        }
        DataLocalManager.setListBucket(lstBucket, Constant.LIST_ALL_PIC)
    }

    fun getPicAssets(context: Context, name: String): ArrayList<PicModel> {
        val lstPicAsset = ArrayList<PicModel>()
        try {
            val f = context.assets.list(name)
            for (str in listOf(*f!!)) {
                lstPicAsset.add(PicModel(str.replace(".webp", ""), "tattoo_background", -1F, str, false))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lstPicAsset
    }
}