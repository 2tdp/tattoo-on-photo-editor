package com.tattoo.tattoomaker.on.myphoto.sharepref

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tattoo.tattoomaker.on.myphoto.model.MultiTattooPremiumModel
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.model.picture.BucketPicModel
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class DataLocalManager {
    private var mySharedPreferences: MySharePreferences? = null

    companion object {
        private var instance: DataLocalManager? = null
        fun init(context: Context) {
            instance = DataLocalManager()
            instance!!.mySharedPreferences = MySharePreferences(context)
        }

        private fun getInstance(): DataLocalManager? {
            if (instance == null) instance = DataLocalManager()
            return instance
        }

        fun setFirstInstall(key: String?, isFirst: Boolean) {
            getInstance()!!.mySharedPreferences!!.putBooleanValue(key, isFirst)
        }

        fun getFirstInstall(key: String?): Boolean {
            return getInstance()!!.mySharedPreferences!!.getBooleanValue(key)
        }

        fun setCheck(key: String?, volumeOn: Boolean) {
            getInstance()!!.mySharedPreferences!!.putBooleanValue(key, volumeOn)
        }

        fun getCheck(key: String?): Boolean {
            return getInstance()!!.mySharedPreferences!!.getBooleanValue(key)
        }

        fun setOption(option: String?, key: String?) {
            getInstance()!!.mySharedPreferences!!.putStringwithKey(key, option)
        }

        fun getOption(key: String?): String? {
            return getInstance()!!.mySharedPreferences!!.getStringwithKey(key, "")
        }

        fun setInt(count: Int, key: String?) {
            getInstance()!!.mySharedPreferences!!.putIntWithKey(key, count)
        }

        fun getInt(key: String?): Int {
            return getInstance()!!.mySharedPreferences!!.getIntWithKey(key, -1)
        }

        fun setPicture(picture: PicModel, key: String?) {
            val gson = Gson()
            val jsonObject = gson.toJsonTree(picture).asJsonObject
            val json = jsonObject.toString()
            getInstance()!!.mySharedPreferences!!.putStringwithKey(key, json)
        }

        fun getPicture(key: String?): PicModel? {
            val strJson = getInstance()!!.mySharedPreferences!!.getStringwithKey(key, "")
            var picture: PicModel? = null
            val gson = Gson()
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(strJson!!)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (jsonObject != null) picture = gson.fromJson(jsonObject.toString(), PicModel::class.java)

            return picture
        }

        fun setProject(project: ProjectModel?, key: String) {
            val gson = Gson()
            val jsonObject: JsonObject
            var json = ""
            if (project != null) {
                jsonObject = gson.toJsonTree(project).asJsonObject
                json = jsonObject.toString()
            }
            getInstance()!!.mySharedPreferences!!.putStringwithKey(key, json)
        }

        fun getProject(key: String): ProjectModel? {
            val strJson = getInstance()!!.mySharedPreferences!!.getStringwithKey(key, "")
            var project: ProjectModel? = null
            val gson = Gson()
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(strJson!!)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (jsonObject != null) project =
                gson.fromJson(jsonObject.toString(), ProjectModel::class.java)
            return project
        }

        fun setMultiTattooPremium(lstTattoo: ArrayList<MultiTattooPremiumModel>, key: String) {
            val gson = Gson()
            val jsonArray = gson.toJsonTree(lstTattoo).asJsonArray
            val json = jsonArray.toString()
            getInstance()!!.mySharedPreferences!!.putStringwithKey(key, json)
        }

        fun getMultiTattooPremium(key: String): ArrayList<MultiTattooPremiumModel> {
            val gson = Gson()
            var jsonObject: JSONObject
            val lstTattoo = ArrayList<MultiTattooPremiumModel>()
            val strJson = getInstance()!!.mySharedPreferences!!.getStringwithKey(key, "")
            try {
                val jsonArray = JSONArray(strJson)
                for (i in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i)
                    lstTattoo.add(gson.fromJson(jsonObject.toString(), MultiTattooPremiumModel::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return lstTattoo
        }

        fun setListProject(context: Context, lstProject: ArrayList<ProjectModel>, name: String) {
            val gson = Gson()
            val jsonArray = gson.toJsonTree(lstProject).asJsonArray
            val json = jsonArray.toString()
            Utils.writeToFileText(context, json, name)
        }

        fun getListProject(context: Context, name: String): ArrayList<ProjectModel> {
            val gson = Gson()
            var jsonObject: JSONObject
            val lstProject = ArrayList<ProjectModel>()

            val strJson = Utils.readFromFile(context, name)
            try {
                val jsonArray = JSONArray(strJson)
                for (i in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i)
                    lstProject.add(gson.fromJson(jsonObject.toString(), ProjectModel::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return lstProject
        }

        fun setListBucket(lstBucket: ArrayList<BucketPicModel>, key: String) {
            val gson = Gson()
            val jsonArray = gson.toJsonTree(lstBucket).asJsonArray
            val json = jsonArray.toString()
            getInstance()!!.mySharedPreferences!!.putStringwithKey(key, json)
        }

        fun getListBucket(key: String): ArrayList<BucketPicModel> {
            val gson = Gson()
            var jsonObject: JSONObject
            val lstBucket: ArrayList<BucketPicModel> = ArrayList()
            val strJson = getInstance()!!.mySharedPreferences!!.getStringwithKey(key, "")
            try {
                val jsonArray = JSONArray(strJson)
                for (i in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i)
                    lstBucket.add(gson.fromJson(jsonObject.toString(), BucketPicModel::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return lstBucket
        }
    }
}