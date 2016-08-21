package vn.com.vietatech.lib;

import android.content.Context;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;
import java.util.Set;

import vn.com.vietatech.dto.Config;

public class ExchangeService {
    private static ExchangeService instance = null;

    private Context context;

    private Config config;

    protected SoapObject request;

    protected static String NAMESPACE;
    protected static String URL;

    private String method = "";

    public static ExchangeService getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new ExchangeService(context);
        }
        return instance;
    }

    protected ExchangeService(Context context) throws Exception {
        // Exists only to defeat instantiation.
        this.context = context;

        ConfigUtils configUtils = ConfigUtils.getInstance(context);
        config = configUtils.displaySharedPreferences();

        String serviceUri = configUtils.getServiceUrl();

        NAMESPACE = "http://tempuri.org/";
//        URL = config.getServer() + ":" + config.getPort() + serviceUri;
        URL = "http://113.161.79.56/VNPostService/VNPostService.asmx";

        if (!Utils.isNetworkAvailable(context)) {
            throw new Exception("No Internet Connection");
        }
    }

    protected String getMethod() {
        return method;
    }

    protected String getSoapAction() {
        return NAMESPACE + getMethod();
    }

    private final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    public Object callService(Map<String, String> params) throws Exception {

        request = new SoapObject(NAMESPACE, getMethod());

        // Get keys.
        Set<String> keys = params.keySet();
        // Loop over String keys.
        for (String key : keys) {
            request.addProperty(key, params.get(key).toString());
        }

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(getSoapAction(), envelope);
            if (envelope.bodyIn instanceof SoapFault) {
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                throw new Exception(str);
            } else {
                return envelope.getResponse();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
