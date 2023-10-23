package br.com.backend.modules.pagamento.hook.models;

import br.com.backend.modules.pagamento.hook.models.utils.charge.ChargeBackWebHook;
import br.com.backend.modules.pagamento.hook.models.utils.creditcard.CreditCardWebHook;
import br.com.backend.modules.pagamento.hook.models.utils.discount.DiscountWebHook;
import br.com.backend.modules.pagamento.hook.models.enums.BillingTypeEnum;
import br.com.backend.modules.pagamento.hook.models.utils.fine.FineWebHook;
import br.com.backend.modules.pagamento.hook.models.utils.interest.InterestWebHook;
import br.com.backend.modules.pagamento.hook.models.utils.split.SplitWebHook;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoWebHookRequest {
    private String object;
    private String id;
    private String dateCreated;
    private String customer;
    private String subscription;
    private String installment;
    private String paymentLink;
    private String dueDate;
    private String originalDueDate;
    private Double value;
    private Double netValue;
    private Double originalValue;
    private Double interestValue;
    private String description;
    private String externalReference;
    private BillingTypeEnum billingType;
    private String status;
    private String pixTransaction;
    private String confirmedDate;
    private String paymentDate;
    private String clientPaymentDate;
    private String installmentNumber;
    private String creditDate;
    private String estimatedCreditDate;
    private String invoiceUrl;
    private String bankSlipUrl;
    private String transactionReceiptUrl;
    private String invoiceNumber;
    private String deleted;
    private String antecipated;
    private String antecipable;
    private String lastInvoiceViewedDate;
    private String lastBankSlipViewedDate;
    private String postalService;
    private CreditCardWebHook creditCard;
    private DiscountWebHook discount;
    private FineWebHook fine;
    private InterestWebHook interest;
    private List<SplitWebHook> split;
    private ChargeBackWebHook chargeBack;
    private String refunds;
}
