package com.flacko.payment.webapp.rest;

import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.service.PaymentDirection;
import com.flacko.payment.service.PaymentListBuilder;
import com.flacko.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private static final String MERCHANT_ID = "merchant_id";
    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String CARD_ID = "card_id";
    private static final String DIRECTION = "direction";
    private static final String CURRENT_STATE = "current_state";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final PaymentService paymentService;
    private final PaymentRestMapper paymentRestMapper;

    @GetMapping
    public List<PaymentResponse> list(@RequestParam(MERCHANT_ID) Optional<String> merchantId,
                                      @RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                      @RequestParam(CARD_ID) Optional<String> cardId,
                                      @RequestParam(DIRECTION) Optional<PaymentDirection> direction,
                                      @RequestParam(CURRENT_STATE) Optional<PaymentState> currentState,
                                      @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                      @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        PaymentListBuilder builder = paymentService.list();
        merchantId.ifPresent(builder::withMerchantId);
        traderTeamId.ifPresent(builder::withTraderTeamId);
        cardId.ifPresent(builder::withCardId);
        direction.ifPresent(builder::withDirection);
        currentState.ifPresent(builder::withCurrentState);
        return builder.build()
                .stream()
                .map(paymentRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse get(@PathVariable String paymentId) throws PaymentNotFoundException {
        return paymentRestMapper.mapModelToResponse(paymentService.get(paymentId));
    }

    @PostMapping("/incoming")
    public PaymentCreateResponse createIncoming(@RequestBody PaymentCreateRequest paymentCreateRequest) {
        return
    }

    @PostMapping("/outgoing")
    public PaymentCreateResponse createOutgoing(@RequestBody PaymentCreateRequest paymentCreateRequest) {
        return
    }

}
