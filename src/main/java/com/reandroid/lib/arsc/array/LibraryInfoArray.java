package com.reandroid.lib.arsc.array;

import com.reandroid.lib.arsc.base.BlockArray;
import com.reandroid.lib.arsc.io.BlockReader;
import com.reandroid.lib.arsc.item.IntegerItem;
import com.reandroid.lib.arsc.value.LibraryInfo;
import com.reandroid.lib.json.JSONConvert;
import com.reandroid.lib.json.JSONArray;
import com.reandroid.lib.json.JSONObject;

import java.io.IOException;

public class LibraryInfoArray extends BlockArray<LibraryInfo> implements JSONConvert<JSONArray> {
    private final IntegerItem mInfoCount;
    public LibraryInfoArray(IntegerItem infoCount){
        this.mInfoCount=infoCount;
    }
    public LibraryInfo getOrCreate(int pkgId){
        LibraryInfo info=getById(pkgId);
        if(info!=null){
            return info;
        }
        int index=childesCount();
        ensureSize(index+1);
        info=get(index);
        info.setPackageId(pkgId);
        return info;
    }
    public LibraryInfo getById(int pkgId){
        for(LibraryInfo info:listItems()){
            if(pkgId==info.getPackageId()){
                return info;
            }
        }
        return null;
    }
    @Override
    public LibraryInfo newInstance() {
        return new LibraryInfo();
    }
    @Override
    public LibraryInfo[] newInstance(int len) {
        return new LibraryInfo[len];
    }
    @Override
    protected void onRefreshed() {
        mInfoCount.set(childesCount());
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        setChildesCount(mInfoCount.get());
        super.onReadBytes(reader);
    }
    @Override
    public JSONArray toJson() {
        JSONArray jsonArray=new JSONArray();
        int i=0;
        for(LibraryInfo libraryInfo:listItems()){
            JSONObject jsonObject= libraryInfo.toJson();
            if(jsonObject==null){
                continue;
            }
            jsonArray.put(i, jsonObject);
            i++;
        }
        return jsonArray;
    }
    @Override
    public void fromJson(JSONArray json) {
        clearChildes();
        if(json==null){
            return;
        }
        int length= json.length();
        ensureSize(length);
        for (int i=0;i<length;i++){
            JSONObject jsonObject=json.getJSONObject(i);
            LibraryInfo libraryInfo=get(i);
            libraryInfo.fromJson(jsonObject);
        }
    }
    public void merge(LibraryInfoArray infoArray){
        if(infoArray==null||infoArray==this||infoArray.childesCount()==0){
            return;
        }
        for(LibraryInfo info:infoArray.listItems()){
            LibraryInfo exist=getOrCreate(info.getPackageId());
            exist.merge(info);
        }
    }
}
