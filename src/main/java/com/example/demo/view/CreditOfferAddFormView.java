package com.example.demo.view;

import com.example.demo.entitys.Client;
import com.example.demo.entitys.Credit;
import com.example.demo.entitys.CreditOffer;
import com.example.demo.services.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreditOfferAddFormView extends VerticalLayout {

    private CreditOfferView creditOfferView;
    private CreditOfferService creditOfferServiceImpl;
    private ComboBox<String> clientPassportLabel = new ComboBox<>("Client passport");
    private ComboBox<String> creditNameLabel = new ComboBox<>("Credit name");
    private TextField sumCredit = new TextField("Sum credit");
    private TextField creditTerm = new TextField("Credit term");

    public CreditOfferAddFormView(CreditOfferView creditOfferView,
                                  CreditOfferService creditOfferServiceImpl,
                                  ClientService clientService,
                                  CreditService creditService){
        this.creditOfferView = creditOfferView;
        this.creditOfferServiceImpl = creditOfferServiceImpl;
        setHelperText();
        List<Client> clients = clientService.findAll();
        clientPassportLabel.setItems(clients
                .stream()
                .map(Client::getPassport)
                .collect(Collectors.toList()));
        add(clientPassportLabel);

        List<Credit> credits = creditService.findAll();
        creditNameLabel.setItems(credits
                .stream()
                .map(Credit::getCreditName)
                .collect(Collectors.toList()));
        add(creditNameLabel);

        HorizontalLayout layout = new HorizontalLayout(creditNameLabel, clientPassportLabel, sumCredit, creditTerm);
        add(layout);
        Button add = new Button("Add credit offer", clickEvent -> addCreditOffer());
        add(add);
    }

    private void setHelperText(){
        sumCredit.setHelperText("The credit amount must not exceed the limit");
        creditTerm.setHelperText("Positive number");
    }

    private void addCreditOffer() {
        if(isEmptyFields()){
            Notification.show("Please enter all details");
        } else {
            Credit credit = new Credit();
            credit.setCreditName(creditNameLabel.getValue());
            Client client = new Client();
            client.setPassport(clientPassportLabel.getValue());
            CreditOffer creditOffer = new CreditOffer(client, credit, Long.parseLong(sumCredit.getValue()), Integer.parseInt(creditTerm.getValue()));
            creditOfferServiceImpl.addCreditOffer(creditOffer);
            creditOfferView.updateList();
        }
    }

    private boolean isEmptyFields(){
        return sumCredit.getValue().equals("") && creditTerm.getValue().equals("");
    }
}
