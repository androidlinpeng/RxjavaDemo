package msgcopy.com.rxjavademo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liang on 2017/3/27.
 */

public interface MovieService {
    @GET("top250")
    Observable<MovieBean> getHttpData(@Query("start") int start, @Query("count") int count);
}
