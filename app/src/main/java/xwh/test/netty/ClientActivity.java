package xwh.test.netty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import xwh.test.netty.client.NettyClientBootstrap;
import xwh.test.netty.server.Const;
import xwh.test.netty.server.Message;

public class ClientActivity extends AppCompatActivity {

	private EditText mEditText;
	private NettyClientBootstrap mNettyClientBootstrap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		mEditText = this.findViewById(R.id.input_ip);

		this.findViewById(R.id.bt_click).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				connect();
			}
		});

		this.findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				send();
			}
		});
	}


	public void connect() {
		final String serverIp = mEditText.getText().toString();
		try {
			mNettyClientBootstrap = new NettyClientBootstrap(Const.SERVER_PORT, serverIp);
			mNettyClientBootstrap.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private int s = 1;

	public void send() {
		try {
			Message req = new Message();
			req.setType(1);
			req.setSequence(s);
			req.setBody(new Date().toString());
			mNettyClientBootstrap.sendMessage(req);
			s++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		mNettyClientBootstrap.disconnect();
		super.onDestroy();
	}
}
