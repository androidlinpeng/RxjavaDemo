package msgcopy.com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SecondAvtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_avtivity);

        MetaChangedEvent metaChangedEvent = new MetaChangedEvent(100,
                "发送消息", "main你好");
        RxBus.getInstance().post(metaChangedEvent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
    }
}
