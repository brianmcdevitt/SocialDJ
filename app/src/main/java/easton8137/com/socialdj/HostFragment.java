package easton8137.com.socialdj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;

/**
 * Created by Brian on 4/11/2015.
 */
public class HostFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_host, parent, false);
        return v;
    }
}
