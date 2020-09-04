package com.danny.feedmeappadminstaff.Common;

import android.net.Uri;

import com.danny.feedmeappadminstaff.Model.Request;

public class Common {

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static Request currentRequest;

    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On My Way";
        else
            return "Shipped";
    }

}
