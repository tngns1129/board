package com.semo.myapplication

import android.content.DialogInterface
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentListAdapter (
    val contents: ArrayList<CommentData>,
    val user:UserData,
    var sharedPreferences : SharedPreferences,
    var block_list:ArrayList<Int>,
    ) : RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>(){

    private lateinit var retrofit:Retrofit
    private lateinit var boardService: BoardService
    private lateinit var editor : SharedPreferences.Editor

    class CommentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var comment: TextView = itemView.findViewById(R.id.comment)
        val author: TextView = itemView.findViewById(R.id.post_author)
        val date: TextView = itemView.findViewById(R.id.date)
        val report: TextView = itemView.findViewById(R.id.report)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_comment_list, parent, false)
        retrofit = Retrofit.Builder()
            .baseUrl(parent.context.resources.getString(R.string.server_adress))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        boardService = retrofit.create(BoardService::class.java)
        return CommentListAdapter.CommentViewHolder(view)

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var d:String
        var t:String
        var date:String
        var deleteData:DeleteData? = null

        if(contents[position].updated_date.toString().length>15) {
            if (contents[position].updated_date.toString().substring(5 until 6)
                    .equals("0")
            ) // ??? 10?????????
                d = contents[position].updated_date.toString().substring(6 until 7)     //??? 1?????? ??????
            else
                d = contents[position].updated_date.toString().substring(5 until 7)     //??? 2?????? ??????

            d = d + "/" + contents[position].updated_date.toString().substring(8 until 10)

            Log.d("date", contents[position].updated_date.toString())

            t = contents[position].updated_date.toString().substring(11 until 16)
            date = d + " " + t
        } else{
            date = contents[position].updated_date.toString()
        }
        holder.comment.text = contents[position].content
        holder.author.text = contents[position].user?.username
        holder.date.text = date

        holder.itemView.setOnClickListener {

        }
        holder.report.setOnClickListener {
            var colorsArray: Array<String> = arrayOf(
                holder.itemView.resources.getString(R.string.report_list_1),
                holder.itemView.resources.getString(R.string.report_list_2),
                holder.itemView.resources.getString(R.string.report_list_3),
            ) // ???????????? ????????? Array
            val builder = AlertDialog.Builder(holder.itemView.context)
                .setItems(colorsArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // ????????? ?????? 'which'??? ????????? position??? ???????????????.
                        if(which == 0) {
                            editor = sharedPreferences.edit()
                            contents[position].id?.let { it1 -> block_list.add(it1) }
                            editor.putString("id", Gson().toJson(block_list))
                            editor.commit()
                            contents.removeAt(holder.bindingAdapterPosition)
                        } else if(which ==1){
                            editor = sharedPreferences.edit()
                            contents[position].id?.let { it1 -> block_list.add(it1) }
                            editor.putString("id", Gson().toJson(block_list))
                            editor.commit()
                            contents.removeAt(holder.bindingAdapterPosition)
                        } else if(which ==2){
                            editor = sharedPreferences.edit()
                            contents[position].id?.let { it1 -> block_list.add(it1) }
                            editor.putString("id", Gson().toJson(block_list))
                            editor.commit()
                            contents.removeAt(holder.bindingAdapterPosition)
                        }
                        notifyDataSetChanged()
                    })
            builder.show()
        }
        holder.itemView.setOnLongClickListener {
            var menuArray: Array<String> = arrayOf(
                holder.itemView.resources.getString(R.string.delete),
                holder.itemView.resources.getString(R.string.report),
            ) // ???????????? ????????? Array
            var reportArray: Array<String> = arrayOf(
                holder.itemView.resources.getString(R.string.report_list_1),
                holder.itemView.resources.getString(R.string.report_list_2),
                holder.itemView.resources.getString(R.string.report_list_3),
            ) // ???????????? ????????? Array
            val builder = AlertDialog.Builder(holder.itemView.context)
                .setItems(menuArray,
                    DialogInterface.OnClickListener { dialog, which ->
                        // ????????? ?????? 'which'??? ????????? position??? ???????????????.
                        if(which == 0) {    //?????? ??????
                            boardService.deletecommentview(contents[position].id, contents[position].user?.id,).enqueue(object :
                                Callback<DeleteData> {
                                override fun onResponse(
                                    call: Call<DeleteData>,
                                    response: Response<DeleteData>
                                ) {
                                    deleteData = response.body()
                                    if (deleteData?.code.equals("000")) {
                                        contents.removeAt(holder.bindingAdapterPosition)
                                        notifyDataSetChanged()
                                        Toast.makeText(holder.itemView.context,holder.itemView.resources.getString(R.string.deleted),Toast.LENGTH_SHORT).show()
                                    } else if (deleteData?.code.equals("001")) {
                                        Toast.makeText(holder.itemView.context,holder.itemView.resources.getString(R.string.authormiss),Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<DeleteData>, t: Throwable) {
                                }
                            })

                        } else if(which ==1){   //?????? ??????
                            val builder = AlertDialog.Builder(holder.itemView.context)
                                .setItems(reportArray,
                                    DialogInterface.OnClickListener { dialog, which ->
                                        // ????????? ?????? 'which'??? ????????? position??? ???????????????.
                                        if(which == 0) {
                                            editor = sharedPreferences.edit()
                                            contents[position].id?.let { it1 -> block_list.add(it1) }
                                            editor.putString("id", Gson().toJson(block_list))
                                            editor.commit()
                                            contents.removeAt(holder.bindingAdapterPosition)
                                        } else if(which ==1){
                                            editor = sharedPreferences.edit()
                                            contents[position].id?.let { it1 -> block_list.add(it1) }
                                            editor.putString("id", Gson().toJson(block_list))
                                            editor.commit()
                                            contents.removeAt(holder.bindingAdapterPosition)
                                        } else if(which ==2){
                                            editor = sharedPreferences.edit()
                                            contents[position].id?.let { it1 -> block_list.add(it1) }
                                            editor.putString("id", Gson().toJson(block_list))
                                            editor.commit()
                                            contents.removeAt(holder.bindingAdapterPosition)
                                        }
                                        notifyDataSetChanged()
                                    })
                            builder.show()
                        } else if(which ==2){
                        }
                    })
            // ?????????????????? ????????????
            builder.show()
            return@setOnLongClickListener true
        }
    }
    override fun getItemCount(): Int {
        return contents.size
    }
}