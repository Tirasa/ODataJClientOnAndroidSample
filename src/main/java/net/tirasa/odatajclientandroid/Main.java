package net.tirasa.odatajclientandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataRetrieveRequestFactory;
import com.msopentech.odatajclient.engine.data.ODataEntity;
import com.msopentech.odatajclient.engine.uri.ODataURIBuilder;

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

                final ODataEntity product = ODataRetrieveRequestFactory.getEntityRequest(new ODataURIBuilder(
                        "http://services.odata.org/v3/(S(ile3zwcmuiizgc0tozjn4202))/OData/OData.svc").
                        appendEntitySetSegment("Products").appendKeySegment(0).build()).execute().getBody();

                text.append("ID: ").
                        append(product.getProperty("ID").getValue().asPrimitive().toString()).
                        append('\n');
                text.append("Name: ").
                        append(product.getProperty("Name").getValue().asPrimitive().toString()).
                        append('\n');
                text.append("Description: ").
                        append(product.getProperty("Description").getValue().asPrimitive().toString()).
                        append('\n');
                text.append("Release date: ").
                        append(product.getProperty("ReleaseDate").getValue().asPrimitive().toString()).
                        append('\n');
                text.append("Rating: ").
                        append(product.getProperty("Rating").getValue().asPrimitive().toString()).
                        append('\n');
                text.append("Price: ").
                        append(product.getProperty("Price").getValue().asPrimitive().toString()).
                        append('\n');

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
