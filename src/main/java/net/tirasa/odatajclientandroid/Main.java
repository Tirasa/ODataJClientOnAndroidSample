package net.tirasa.odatajclientandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataEntityRequest;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataRetrieveRequestFactory;
import com.msopentech.odatajclient.engine.data.ODataEntity;
import com.msopentech.odatajclient.engine.format.ODataPubFormat;
import com.msopentech.odatajclient.engine.uri.ODataURIBuilder;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Main extends Activity implements OnClickListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.my_button).setOnClickListener(this);
    }

    @Override
    public void onClick(final View arg0) {
        final Button b = (Button) findViewById(R.id.my_button);
        b.setClickable(false);
        new LongRunningGetIO().execute();
    }

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(final Void... params) {
            final StringBuilder text = new StringBuilder();

            try {
//                final EdmMetadata metadata = ODataRetrieveRequestFactory.getMetadataRequest(
//                        "http://services.odata.org/v3/(S(ile3zwcmuiizgc0tozjn4202))/OData/OData.svc").
//                        execute().getBody();
//
//                text.append("Metadata schema namespace: ").append(metadata.getSchema(0).getNamespace()).
//                        append('\n').append('\n');

                final ODataEntityRequest req = ODataRetrieveRequestFactory.getEntityRequest(new ODataURIBuilder(
                        "http://services.odata.org/v3/(S(ile3zwcmuiizgc0tozjn4202))/OData/OData.svc").
                        appendEntitySetSegment("Products").appendKeySegment(0).build());
                req.setFormat(ODataPubFormat.ATOM);

                try {
                    final ODataEntity product = req.execute().getBody();

                    text.append("ID: ").
                            append(product.getProperty("ID").getValue().asPrimitive().toString()).
                            append('\n');

                    if (product.getProperty("Name") != null) {
                        text.append("Name: ").
                                append(product.getProperty("Name").getValue().asPrimitive().toString()).
                                append('\n');
                    }
                    if (product.getProperty("Description") != null) {
                        text.append("Description: ").
                                append(product.getProperty("Description").getValue().asPrimitive().toString()).
                                append('\n');
                    }
                    if (product.getProperty("ReleaseDate") != null) {
                        text.append("Release date: ").
                                append(product.getProperty("ReleaseDate").getValue().asPrimitive().toString()).
                                append('\n');
                    }
                    if (product.getProperty("Rating") != null) {
                        text.append("Rating: ").
                                append(product.getProperty("Rating").getValue().asPrimitive().toString()).
                                append('\n');
                    }
                    if (product.getProperty("Price") != null) {
                        text.append("Price: ").
                                append(product.getProperty("Price").getValue().asPrimitive().toString()).
                                append('\n');
                    }
                } catch (Throwable t) {
                    StringWriter sw = new StringWriter();
                    t.printStackTrace(new PrintWriter(sw));
                    text.append(sw.toString());
                }

                return text.toString();
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }

        @Override
        protected void onPostExecute(final String results) {
            if (results != null) {
                final EditText et = (EditText) findViewById(R.id.my_edit);
                et.setText(results);
            }
            final Button b = (Button) findViewById(R.id.my_button);
            b.setClickable(true);
        }
    }
}
