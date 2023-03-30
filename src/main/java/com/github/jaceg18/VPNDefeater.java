package com.github.jaceg18;

import com.github.jaceg18.providers.VPNProvider;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
@SuppressWarnings("all")
public class VPNDefeater {

    VPNProvider provider;
    Detection detection;
    public VPNDefeater(VPNProvider provider){
        this.provider = provider;
        this.detection = new Detection(provider);
    }
    public boolean isVPN(String ip) throws Exception {
       return detection.isVPN(ip);
    }
    public String getResponseAsString(String ip) throws Exception {
        return detection.getResponseAsString(ip);
    }
    // Detection settings
    public void setUseVpn(boolean var) {
        detection.useVpn = (var) ? 1 : 0;
    }
    public void setUseAsn(boolean var) {
        detection.useAsn = (var) ? 1 : 0;
    }
    public void setUseNode(boolean var) {
        detection.useNode = (var) ? 1 : 0;
    }
    public void setUseTime(boolean var) {
        detection.useTime = (var) ? 1 : 0;
    }
    public void setUseInf(boolean var) {
        detection.useInf = (var) ? 1 : 0;
    }
    public void setUsePort(boolean var) {
        detection.usePort = (var) ? 1 : 0;
    }
    public void setUseSeen(boolean var) {
        detection.useSeen = (var) ? 1 : 0;
    }
    public void setUseDays(int var) {
        detection.useDays = var;
    }
    public void setTag(String var) {
        detection.tag = var;
    }
    public void set_api_timeout(int timeout) {
        detection.api_timeout = timeout;
    }

    private static class Detection {
        VPNProvider vpnProvider;
        String status, node, asn, provider, country, isocode, proxy, type, port, last_seen_human, last_seen_unix, query_time, message, error, tag;
        int useVpn = 1, useAsn = 0, useNode = 0, useTime = 0, useInf = 0, usePort = 0, useSeen = 0, useDays = 0, api_timeout = 5000;;
        public Detection(VPNProvider provider){
            this.vpnProvider = provider;
        }
        private boolean isVPN(String ip) throws Exception {
            parseResults(ip);
            return proxy.equalsIgnoreCase("yes");
        }
        @SuppressWarnings("unused")
        private String getResponseAsString(String ip) throws Exception {
            String query_url = get_query_url(ip);
            return query(query_url);
        }
        private void parseResults(String ip) throws Exception {
            ip = ip.split(":")[0];
            ip = (ip.contains("/")) ? ip.split("/")[1] : ip;

            String query_url = get_query_url(ip);
            String query_result = query(query_url);

            JSONParser parser = new JSONParser();
            JSONObject main = (JSONObject) parser.parse(query_result);
            JSONObject sub = (JSONObject) main.get(ip);

            if (sub == null) return;

            status = (String) main.get("status");
            node = (String) main.get("node");
            asn = (String) sub.get("asn");
            provider = (String) sub.get("provider");
            country = (String) sub.get("country");
            isocode = (String) sub.get("isocode");
            proxy = (String) sub.get("proxy");
            type = (String) sub.get("type");
            port = (String) sub.get("port");
            last_seen_human = (String) sub.get("last seen human");
            last_seen_unix = (String) sub.get("last seen unix");
            query_time = (String) main.get("query time");
            message = (String) main.get("message");
            error = (String) sub.get("error");
        }
        private String get_query_url(String ip){
            String url = vpnProvider.getURL();
            String key = vpnProvider.getKey();
            return url + ip + "?key=" + key + "&vpn=" + useVpn + "&asn=" + useAsn + "&node=" + useNode + "&time=" + useTime
                    + "&inf=" + useInf + "&port=" + usePort + "&seen=" + useSeen + "&days=" + useDays + "&tag=" + tag;
        }
        private String query(String url) throws Exception {
            StringBuilder response = new StringBuilder();
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            connection.setConnectTimeout(api_timeout);
            connection.setDoInput(true);
            connection.setRequestProperty(vpnProvider.getKey(), tag);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                while ((url = in.readLine()) != null)
                    response.append(url);
            }
            return response.toString();
        }

    }
}
