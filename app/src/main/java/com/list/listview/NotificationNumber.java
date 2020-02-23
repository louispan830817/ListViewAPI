package com.list.listview;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 *
 */
public class NotificationNumber {
    String avatar_url;
    String login;
    String site_admin;

    public NotificationNumber(String avatar_url, String login, String site_admin) {
        this.avatar_url = avatar_url;
        this.login = login;
        this.site_admin = site_admin;
    }

    public NotificationNumber(JSONObject object) {
        try {
            avatar_url = object.getString("avatar_url");
            login = object.getString("login");
            site_admin = object.getString("site_admin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSite_admin() {
        return site_admin;
    }

    public void setSite_admin(String site_admin) {
        this.site_admin = site_admin;
    }

}




