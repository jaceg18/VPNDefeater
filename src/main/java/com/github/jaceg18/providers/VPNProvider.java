package com.github.jaceg18.providers;

public abstract class VPNProvider {

    String api_url;
    String api_key;
    public VPNProvider(String api_key, String api_url){
        this.api_key = api_key;
        this.api_url = api_url;
    }
    public String getURL(){
        return api_url;
    }
    public String getKey(){
        return api_key;
    }

}
