package com.github.jaceg18.providers;

public class ProxyCheck extends VPNProvider {
    public ProxyCheck(String api_key){
        super(api_key, "http://proxycheck.io/v2/");
    }
    public String getURL(){
        return super.api_url;
    }
    public String getKey(){
        return super.api_key;
    }

}
