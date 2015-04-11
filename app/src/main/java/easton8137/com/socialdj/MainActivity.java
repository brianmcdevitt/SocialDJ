package easton8137.com.socialdj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "c66de50901224286b0d57255b03d65e7";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "socialdj://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    private String[] arraySongs = {"spotify:track:7wqSzGeodspE3V6RBD5W8L", "spotify:track:3twQx3psUMJKj4wna5d1zU", "spotify:track:2PIvq1pGrUjY007X5y1UpM",
            "spotify:track:32OlwWuMpZ6b0aN2RZOeMS", "spotify:track:34gCuhDGsG4bRPIf9bb02f", "spotify:track:5eWgDlp3k6Tb5RD8690s6I", "spotify:track:78TTtXnFQPzwqlbtbwqN0y",
            "spotify:track:1ip2IGDWMrUmlaepEbWlL8", "spotify:track:6N9ZOtguCnnrvwH7zD82WJ", "spotify:track:66hayvUbTotekKU3H4ta1f", "spotify:track:0HFx7PLqzGxSfN59j3UHmR",
            "spotify:track:4kbj5MwxO1bq9wjT5g9HaA", "spotify:track:7ioiB40H9xKs04QtIso2I3", "spotify:track:5InOp6q2vvx0fShv3bzFLZ", "spotify:track:2bJvI42r8EF3wxjOuDav4r",
            "spotify:track:4iZPNYqzI2L0uwuUKun7Aa", "spotify:track:0aOluBqXYd0rFSCsgDyAWX", "spotify:track:2tpfxAXiI52znho4WE3XFA", "spotify:track:79XrkTOfV1AqySNjVlygpW",
            "spotify:track:3keUgTGEoZJt0QkzTB6kHg"};
    private ArrayList<String> topTwentySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        topTwentySongs = new ArrayList<String>(Arrays.asList(arraySongs));
                        mPlayer.play(topTwentySongs);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}