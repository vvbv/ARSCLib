package com.reandroid.lib.apk;

import com.reandroid.archive.InputSource;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DexFileInputSource extends RenamedInputSource<InputSource> implements Comparable<DexFileInputSource>{
    public DexFileInputSource(String name, InputSource inputSource){
        super(name, inputSource);
    }
    public int getDexNumber(){
        return getDexNumber(getAlias());
    }
    @Override
    public int compareTo(DexFileInputSource source) {
        return Integer.compare(getDexNumber(), source.getDexNumber());
    }
    public static void sort(List<DexFileInputSource> sourceList){
        sourceList.sort(new Comparator<DexFileInputSource>() {
            @Override
            public int compare(DexFileInputSource s1, DexFileInputSource s2) {
                return s1.compareTo(s2);
            }
        });
    }
    public static boolean isDexName(String name){
        return getDexNumber(name)>=0;
    }
    static String getDexName(int i){
        if(i==0){
            return "classes.dex";
        }
        return "classes"+i+".dex";
    }
    static int getDexNumber(String name){
        Matcher matcher=PATTERN.matcher(name);
        if(!matcher.find()){
            return -1;
        }
        String num=matcher.group(1);
        if(num.length()==0){
            return 0;
        }
        return Integer.parseInt(num);
    }
    private static final Pattern PATTERN=Pattern.compile("^classes([0-9]*)\\.dex$");

}