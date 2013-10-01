package net.tirasa.odatajclientandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataEntityRequest;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataRetrieveRequestFactory;
import com.msopentech.odatajclient.engine.data.ODataEntity;
import com.msopentech.odatajclient.engine.data.metadata.EdmMetadata;
import com.msopentech.odatajclient.engine.format.ODataPubFormat;
import com.msopentech.odatajclient.engine.uri.ODataURIBuilder;
import com.msopentech.odatajclient.engine.utils.Configuration;
import com.msopentech.odatajclient.proxy.api.EntityContainerFactory;
import net.tirasa.odatajclientandroid.proxy.odatademo.DemoService;
import net.tirasa.odatajclientandroid.proxy.odatademo.types.Product;

public class Main extends Activity implements OnClickListener {

    private static final String SERVICE_ROOT = "http://services.odata.org/V3/OData/OData.svc";

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

        private void output(final StringBuilder text, final ODataEntity product) {
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
        }

        private void output(final StringBuilder text, final Product product) {
            text.append("ID: ").
                    append(product.getID()).
                    append('\n');

            if (product.getName() != null) {
                text.append("Name: ").
                        append(product.getName()).
                        append('\n');
            }
            if (product.getDescription() != null) {
                text.append("Description: ").
                        append(product.getDescription()).
                        append('\n');
            }
            if (product.getReleaseDate() != null) {
                text.append("Release date: ").
                        append(product.getReleaseDate()).
                        append('\n');
            }
            if (product.getRating() != null) {
                text.append("Rating: ").
                        append(product.getRating()).
                        append('\n');
            }
            if (product.getPrice() != null) {
                text.append("Price: ").
                        append(product.getPrice()).
                        append('\n');
            }
        }

        @Override
        protected String doInBackground(final Void... params) {
            final StringBuilder text = new StringBuilder();

            text.append("[METADATA]").append('\n');
            // ------------------ Metadata ------------------
            try {
                final EdmMetadata metadata = ODataRetrieveRequestFactory.getMetadataRequest(SERVICE_ROOT).
                        execute().getBody();

                text.append("Schema namespace: ").append(metadata.getSchema(0).getNamespace()).append('\n');
            } catch (Exception e) {
                Log.e("ERROR", "METADATA", e);
                text.append(e.getLocalizedMessage()).append('\n');
            }

            // ------------------ JSON (Engine) ------------------
            text.append('\n').append("[JSON - Engine]").append('\n');
            try {
                final ODataEntityRequest jsonReq = ODataRetrieveRequestFactory.getEntityRequest(new ODataURIBuilder(
                        SERVICE_ROOT).appendEntitySetSegment("Products").appendKeySegment(0).build());

                final ODataEntity jsonProduct = jsonReq.execute().getBody();

                output(text, jsonProduct);
            } catch (Exception e) {
                Log.e("ERROR", "JSON - Engine", e);
                text.append(e.getLocalizedMessage()).append('\n');
            }

            // ------------------ ATOM (Engine) ------------------
            text.append('\n').append("[Atom - Engine]").append('\n');
            try {
                final ODataEntityRequest atomReq = ODataRetrieveRequestFactory.getEntityRequest(new ODataURIBuilder(
                        SERVICE_ROOT).appendEntitySetSegment("Products").appendKeySegment(1).build());
                atomReq.setFormat(ODataPubFormat.ATOM);
                final ODataEntity atomProduct = atomReq.execute().getBody();

                output(text, atomProduct);
            } catch (Exception e) {
                Log.e("ERROR", "Atom - Engine", e);
                text.append(e.getLocalizedMessage()).append('\n');
            }


            // ------------------ JSON (Proxy) ------------------
            text.append('\n').append("[JSON - Proxy]").append('\n');
            try {
                final EntityContainerFactory entityContainerFactory = EntityContainerFactory.getInstance(SERVICE_ROOT);
                final DemoService service = entityContainerFactory.getEntityContainer(DemoService.class);

                final Product product = service.getProducts().get(2);

                output(text, product);
                text.append('\n');
            } catch (Exception e) {
                Log.e("ERROR", "JSON - Proxy", e);
                text.append(e.getLocalizedMessage()).append('\n');
            }

            // ------------------ Atom (Proxy) ------------------
            text.append('\n').append("[Atom - Proxy]").append('\n');
            try {
                Configuration.setDefaultPubFormat(ODataPubFormat.ATOM);

                final EntityContainerFactory entityContainerFactory = EntityContainerFactory.getInstance(SERVICE_ROOT);
                final DemoService service = entityContainerFactory.getEntityContainer(DemoService.class);

                final Product product = service.getProducts().get(3);

                output(text, product);
                text.append('\n');
            } catch (Exception e) {
                Log.e("ERROR", "Atom - Proxy", e);
                text.append(e.getLocalizedMessage()).append('\n');
            }

            // ------------------

            return text.toString();
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
