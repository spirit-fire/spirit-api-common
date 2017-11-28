package com.spirit.commons.thd.acmatch.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.StringUtils;

/**
 * com.spirit.commons.thd.acmatch.service class
 *
 * @author guoxiong
 * @date 2017/11/22 18:01
 */
public class AcMatchService {

    private volatile Trie trie;

    public AcMatchService() {

    }

    /**
     * init trie
     */
    public void init(List<String> keywords) {
        Trie.TrieBuilder tb = Trie.builder();
        if(keywords != null && keywords.size() > 0){
            for(String s:keywords){
                // string check
                if(StringUtils.isNullOrEmpty(s)){
                    continue;
                }
                s = s.trim();
                if(StringUtils.isNullOrEmpty(s)){
                    continue;
                }

                tb.addKeyword(s);
            }
            // max length match?
            tb.ignoreOverlaps();
            this.trie = tb.build();
        }
    }

    /**
     * ac match data
     * @param data	text
     * @return	ArrayList<String>
     */
    public ArrayList<String> match(String data){
        ArrayList<String> arrayList = new ArrayList<String>();
        try{
            // ac parse text
            Collection<Emit> emits = this.trie.parseText(data);
            if(!emits.isEmpty()){
                for(Emit emit:emits){
                    arrayList.add(emit.getKeyword());
                }
            }
        }catch(Exception e){
            ApiLogger.error(String.format("AcMatchService match error! data: %s, err_msg: %s.",
                    data, e.getMessage()));
        }
        return arrayList;
    }
}
