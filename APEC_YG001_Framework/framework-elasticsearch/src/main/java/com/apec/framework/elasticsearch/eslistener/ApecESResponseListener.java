package com.apec.framework.elasticsearch.eslistener;

import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.springframework.stereotype.Component;

/**
 * Title:
 *
 * @author yirde  2017/7/10.
 */
@Component
public class ApecESResponseListener implements ResponseListener {


    @Override
    public void onSuccess(Response response) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}
