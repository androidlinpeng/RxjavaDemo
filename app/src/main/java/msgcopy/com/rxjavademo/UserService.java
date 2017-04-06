package msgcopy.com.rxjavademo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liang on 2017/4/6.
 */

public interface UserService {
    @FormUrlEncoded
    @POST("user/register/auto/?channel_id=A1G1Z00110CjcA002A001A001A0010000T")
    Observable<UserEntity> getUserDtat(@Field("type") String name, @Field("reg_ver") String reg_ver,@Field("device") String device);
}
