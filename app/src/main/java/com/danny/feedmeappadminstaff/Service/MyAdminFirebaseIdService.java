package com.danny.feedmeappadminstaff.Service;

import com.danny.feedmeappadminstaff.Common.Common;
import com.danny.feedmeappadminstaff.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyAdminFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //updateToServer(refreshedToken);
    }

    private void updateToServer(String refreshedToken) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("requests");
        Token data = new Token(refreshedToken,false);
        tokens.child(Common.currentRequest.getPhone()).setValue(data);

    }
}
