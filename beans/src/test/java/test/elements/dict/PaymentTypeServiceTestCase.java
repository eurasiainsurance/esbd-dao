package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.PaymentType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.PaymentTypeMapping;
import tech.lapsa.esbd.dao.IElementsService;
import tech.lapsa.esbd.dao.elements.dict.PaymentTypeService.PaymentTypeServiceLocal;

public class PaymentTypeServiceTestCase extends AMappedElementTestCase<PaymentType> {

    @Inject
    private PaymentTypeServiceLocal service;

    public PaymentTypeServiceTestCase() {
	super(PaymentType.class, PaymentTypeMapping.getInstance(), "PAYMENT_ORDER_TYPES", 99999);
    }

    @Override
    IElementsService<PaymentType> service() {
	return service;
    }
}
