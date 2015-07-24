package hepburn.love.crazysheep.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import hepburn.love.crazysheep.Utils.StringUtils;
import hepburn.love.crazysheep.net.NetRequest;

/**
 * base fragment for all fragment extends
 *
 * Created by crazysheep on 15/7/24.
 */
public class BaseFragment extends Fragment {

    private String mOwnUUID = StringUtils.randomUUID();
    protected NetRequest mNetRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNetRequest = new NetRequest(getActivity(), mOwnUUID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mNetRequest.cancel();
        mNetRequest = null;
    }
}
