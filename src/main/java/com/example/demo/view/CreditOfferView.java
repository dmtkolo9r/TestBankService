package com.example.demo.view;

import com.example.demo.VaadinUI;
import com.example.demo.entitys.CreditOffer;
import com.example.demo.entitys.Payment;
import com.example.demo.frontend.CreditOfferEdit;
import com.example.demo.services.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/offers", layout = VaadinUI.class)
public class CreditOfferView extends VerticalLayout {

    private CreditOfferService creditOfferServiceImpl;
    private CreditOfferAddFormView creditOfferAddFormView;
    private CreditOfferEdit creditOfferEdit;
    private PaymentService paymentService;


    private Grid<CreditOffer> creditOfferGrid = new Grid<>(CreditOffer.class);
    private Grid<Payment> paymentGrid = new Grid<>(Payment.class);

    public CreditOfferView(CreditOfferService creditOfferServiceImpl,
                           ClientService clientService,
                           CreditService creditService,
                           PaymentService paymentService){
        this.creditOfferServiceImpl = creditOfferServiceImpl;
        this.paymentService = paymentService;
        creditOfferEdit = new CreditOfferEdit(this,creditOfferServiceImpl, paymentService);
        creditOfferAddFormView = new CreditOfferAddFormView(this, creditOfferServiceImpl, clientService, creditService);
        add(creditOfferAddFormView);
        creditOfferEdit.setCreditOffer(null);
        paymentService.deleteAll();
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configurePaymentGrid();
        HorizontalLayout mainContent = new HorizontalLayout(creditOfferGrid, creditOfferEdit);
        mainContent.setSizeFull();
        add(mainContent);
        add(paymentGrid);
        updateList();
        creditOfferGrid.asSingleSelect().addValueChangeListener(event ->
                creditOfferEdit.setCreditOffer(creditOfferGrid.asSingleSelect().getValue()));
    }

    private void configureGrid() {
        creditOfferGrid.addClassName("credit-grid");
        creditOfferGrid.setSizeFull();
        creditOfferGrid.setColumns("sumCredit", "creditTerm", "sumOfMonth");
        addCustomColumns();
    }

    private void configurePaymentGrid() {
        paymentGrid.addClassName("payment-grid");
        paymentGrid.setSizeFull();
        paymentGrid.setColumns("dateOfPay", "cashBalance", "allSum", "sumOfBody", "sumOfPercent");
    }

    private void addCustomColumns(){
        creditOfferGrid.addColumn(CreditOffer::getNameClient).setHeader("Client name");
        creditOfferGrid.addColumn(CreditOffer::getClientPassport).setHeader("Client passport");
        creditOfferGrid.addColumn(CreditOffer::getNameCredit).setHeader("Credit name");
    }

    public void updateList() {
        creditOfferGrid.setItems(creditOfferServiceImpl.findAll());
        paymentGrid.setItems(paymentService.findAll());
    }
}
