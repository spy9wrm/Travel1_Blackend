package su.project.travel.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TranModelCachingUtils {

    public  ConcurrentHashMap<Integer,String> contentCache = new ConcurrentHashMap<>();
    public  ConcurrentHashMap<Integer,String> collabCache= new ConcurrentHashMap<>();

}
