package com.iulian.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("session")
@Slf4j
public class HobbyHandler implements Serializable {
    private static final long serialVersionUID = 167897865432L;
    
    @Getter
    @Setter
    private List<String> hobbies = new ArrayList<>(5);

    @Getter
    @Setter
    private String hobby;

    public void insertHobby() {
        if (StringUtils.isNotBlank(hobby)) {
            this.hobbies.add(hobby);
            log.info("hobby inserted: {0}", hobby);
        }
    }
}
