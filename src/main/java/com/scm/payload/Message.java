package com.scm.payload;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String content;
    @Builder.Default
    private MessageType type = MessageType.blue;

}
