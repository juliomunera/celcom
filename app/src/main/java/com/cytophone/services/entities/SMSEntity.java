package com.cytophone.services.entities;

import com.cytophone.services.utilities.*;

import android.telephony.SmsMessage;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.Date;

public class SMSEntity implements IEntityBase, Serializable {
    // region private methods declaration
    private String decodedMessage(String Message) {
        if (Utils.isHexadecimal(Message)) {
            String hexMsg = Utils.convertHexToString(Message);
            if (hexMsg.length() > 0) {
                if (Utils.isBase64Encode(hexMsg)) {
                    return Utils.decodeBase64(hexMsg);
                }
            }
        }
        return "";
    }

    private String getActionID() {
        String[] msgParts = this._decodeMessage.split("[|]");
        if( msgParts.length >= 4 ) {
            if (isItOkAction(msgParts[0])) return msgParts[0];
        }
        return "";
    }

    public String getOperation() {
        String action = this.getActionID();
        if (!isItOkAction(action) ) return ACTIONS[0][1];
        else return ACTIONS[Integer.parseInt(action)][1];
    }

    public String getObjectType() {
        String action = this.getActionID();
        if (!isItOkAction(action) ) return ACTIONS[0][0];
        else return ACTIONS[Integer.parseInt(action)][0];
    }

    private String getPlaceID() {
        String[] msgParts = this._decodeMessage.split("[|]");
        return msgParts.length >= 4 ? (isItOkPlaceID(msgParts[1]) ? msgParts[1] : "") : "";
    }

    private Integer getRole() {
        String action = getActionID();
        if(!isItOkAction(action)) return 0;
        else return Integer.parseInt(ACTIONS[Integer.parseInt(action)][2]);
    }

    private String getType() {
        String action = getActionID();
        if(!isItOkAction(action)) return ACTIONS[0][1];
        else return ACTIONS[Integer.parseInt(action)][0];
    }

    private String getNumberByObjectType() throws Exception {
        String s =  this.getObjectType();

        return s.contains("UnlockCode")
               ? this.getUnlockCodeObject().getMsisdn()
               : s.contains("Authorizator") || s.contains("Suscriber")
                ? this.getPartyObject().getNumber()
                : null;
    }

    private boolean isItOkAction(String value) {
        Matcher m = Constants.ACTION_PATTERN.matcher(value);
        return m.find();
    }

    private boolean isItOkPlaceID(String value) {
        Matcher m = Constants.PLACEID_PATTERN.matcher(value);
        return m.find();
    }

    private boolean isItOkMSIDN(String value) {
        Matcher m1 = Constants.MSISDN1_PATTERN.matcher(value);
        Matcher m2 = Constants.MSISDN2_PATTERN.matcher(value);
        Matcher m3 = Constants.MSISDN3_PATTERN.matcher(value);
        return m1.find() || m2.find() || m3.find();
    }

    private boolean isItOkPartyName(String value) {
        Matcher m = Constants.PARTY_NAME_PATTERN.matcher(value);
        return m.find();
    }

    private boolean isItOkUnlockCode(String value) {
        Matcher m = Constants.CODE_PATTERN.matcher(value);
        return m.find();
    }

    private boolean isItOkTimeElapsed(String value) {
        Matcher m = Constants.TIME_ELAPSED_PATTERN.matcher(value);
        return m.find();
    }

    private boolean isItOkPartyMgmtMsg(String[] messageParts)
    {
        return 4 == messageParts.length &
                isItOkAction(messageParts[0]) &
                isItOkPlaceID(messageParts[1]) &
                isItOkMSIDN(messageParts[2]) &
                isItOkPartyName(messageParts[3]);
    }

    private boolean isItOkUnlockMsg(String[] messageParts) {
        return 4 == messageParts.length &
                isItOkAction(messageParts[0]) &
                isItOkMSIDN(messageParts[1]) &
                isItOkUnlockCode(messageParts[2]) &
                isItOkTimeElapsed(messageParts[3]);
    }

    // Public methods declaration
    public String getActionName() {
        return this.getOperation();
    }

    public String getDecodeMessage() {
        return this._decodeMessage;
    }

    public Date getMesageDate() {
        return this._messageDate;
    }

    public PartyEntity getPartyObject() throws Exception {
        String[] msgParts = this._decodeMessage.split("[|]");
        if( !isItOkPartyMgmtMsg(msgParts) ) {
            throw new Exception("El mensaje para el registro de un suscriptor/autorizador no es válido.");
        }
        return new PartyEntity(msgParts[2], msgParts[1], msgParts[3], this.getRole());
    }

    public EventEntity getEventObject() throws Exception {
        String number = this.getNumberByObjectType();

        return new EventEntity(
                this.getSourceNumber(),
                number,
                "SMS",
                this.getMesageDate(),
                this.getTypeName().concat(this.getActionName())
        );
    }

    public String getTypeName() {
        return this.getType();
    }

    public String getRawMesage() {
        return this._rawMessage;
    }

    public UnlockCodeEntity getUnlockCodeObject() throws Exception {
        String[] msgParts = this._decodeMessage.split("[|]");
        if( !isItOkUnlockMsg(msgParts) ) {
            throw new Exception("El mensaje de desbloqueo del dispositivo no es válido.");
        }
        return new UnlockCodeEntity(msgParts[2], msgParts[1], Integer.parseInt(msgParts[3]));
    }

    public String getSourceNumber() {
        return this._sourceNumber;
    }

    // Constructor method
    public SMSEntity(SmsMessage sms) {
        if (null != sms) {
            this._decodeMessage = this.decodedMessage(sms.getDisplayMessageBody());
            this._sourceNumber = sms.getDisplayOriginatingAddress();
            this._messageDate = new Date(sms.getTimestampMillis());
            this._rawMessage = sms.getDisplayMessageBody();
        }
    }

    // region fields declarations
    // Actions by type message.
    private final String[][] ACTIONS = {
            { "none", "none", null },
            { "Authorizator", "insert", "1" },
            { "Authorizator", "update", "1" },
            { "Authorizator", "delete", "1" },
            { "Suscriber", "insert", "2" },
            { "Suscriber", "update", "2" },
            { "Suscriber", "delete", "2" },
            { "UnlockCode", "insert", null }
    };

    private String _decodeMessage;
    private String _sourceNumber;
    private String _rawMessage;
    private Date _messageDate;
    // endregion
}

