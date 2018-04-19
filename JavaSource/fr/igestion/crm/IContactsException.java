package fr.igestion.crm;

public class IContactsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IContactsException(){
    }
    
    public IContactsException(String message){
        super(message);
    }
    
    public IContactsException(Throwable cause){
        super(cause);
    }
    
    public IContactsException(String message,Throwable cause){
        super(message, cause);
    }
}
