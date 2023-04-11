package com.numble.instagram.presentation.dm;

import com.numble.instagram.application.usecase.dm.CreateMessageUsecase;
import com.numble.instagram.dto.request.dm.MessageRequest;
import com.numble.instagram.dto.response.dm.MessageResponse;
import com.numble.instagram.presentation.auth.AuthenticatedUser;
import com.numble.instagram.presentation.auth.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController {

    private final CreateMessageUsecase createMessageUsecase;

    @Login
    @PostMapping("/{toUserId}")
    public MessageResponse sendMessage(@AuthenticatedUser Long fromUserId,
                                       @PathVariable Long toUserId,
                                       @RequestBody MessageRequest messageRequest) {
        return createMessageUsecase.execute(fromUserId, toUserId, messageRequest);
    }
}
