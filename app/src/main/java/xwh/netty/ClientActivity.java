package xwh.netty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import xwh.netty.client.NettyClientBootstrap;
import xwh.netty.client.NettyClientBootstrap.ConnectListener;
import xwh.netty.message.Message;
import xwh.netty.server.Const;

public class ClientActivity extends AppCompatActivity {

	private EditText mEditText;
	private EditText mTextMessage;
	private NettyClientBootstrap mNettyClientBootstrap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		mEditText = this.findViewById(R.id.input_ip);
		mTextMessage = this.findViewById(R.id.input_message);

		this.findViewById(R.id.bt_click).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				connect();
			}
		});

		this.findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int type = 1;
				String content = mTextMessage.getText().toString();
				send(type, content);
			}
		});
	}


	public void connect() {
		final String serverIp = mEditText.getText().toString();
		try {
			mNettyClientBootstrap = new NettyClientBootstrap(Const.SERVER_PORT, serverIp);
			mNettyClientBootstrap.connect(new ConnectListener() {
				@Override
				public void onSuccess() {
					new Toast(ClientActivity.this).makeText(ClientActivity.this, "连接成功", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onFailure(Exception e) {
					new Toast(ClientActivity.this).makeText(ClientActivity.this, "连接失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private int s = 1;

	public void send(int type, String content) {
		try {
			Message req = new Message();
			req.setType(type);
			req.setSequence(s++);
			req.setBody(content);
			mNettyClientBootstrap.sendMessage(req);
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
