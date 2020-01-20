package bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {


    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if(tx.getCommands().size() != 1){
            throw new IllegalArgumentException("Transaction must have one command");
        }
        Command command = tx.getCommand(0); //list of public keys who gonna sign the transaction with the type
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue(); //type of the command

        if(commandType instanceof Register){
            //TODO("Register transaction logic.")
            //Shape constraints --> Numbers of inputs, outputs
            //A new house expect 0 inputs
            if(tx.getInputStates().size() != 0){
                throw new IllegalArgumentException("Registration transaction must have no inputs");
            }
            if(tx.getOutputStates().size() != 1){
                throw new IllegalArgumentException("Registration transaction must have one output");
            }


            //Content constrants --> Inspect the commands, inputs & outputs and checking if they have the right value
            ContractState outputState = tx.getOutput(0);

            if(!(outputState instanceof HouseState)){
                throw new IllegalArgumentException("Output must be a HouseState");
            }
            HouseState houseState = (HouseState) outputState;
            if(houseState.getAdress().length() <= 3){
                throw new IllegalArgumentException("Adress must be longer than 3 characters");
            }
            if(houseState.getOwner().getName().getCountry().equals("Brazil")){
                throw new IllegalArgumentException("Not allowed to register for Brazilian owners");
            }
            //Riquered signer constraints --> Check if the signers has signed
            Party owner = houseState.getOwner();
            PublicKey ownerKey = owner.getOwningKey();
            if(!(requiredSigners.contains(ownerKey))){
                throw new IllegalArgumentException("Owner of the house must register the registration");
            }

        }else if(commandType instanceof Transfer){
            //"SHAPE"
            if(tx.getInputStates().size() != 1){
                throw new IllegalArgumentException("Registration transaction must have one input");
            }
            if(tx.getOutputStates().size() != 1){
                throw new IllegalArgumentException("Registration transaction must have one output");
            }

            //Content
            ContractState input = tx.getInput(0);
            ContractState output = tx.getOutput(0);

            if(!(input instanceof HouseState)){
                throw new IllegalArgumentException("input must be a HouseState");
            }
            if(!(output instanceof HouseState)){
                throw new IllegalArgumentException("Output must be a HouseState");
            }

            HouseState inputHouse = (HouseState) input;
            HouseState outputHouse = (HouseState) output;

            if(!(inputHouse.getAdress().equals(outputHouse.getAdress()))){
                throw new IllegalArgumentException("In a transfer, the adress cant change");
            }
            if(inputHouse.getOwner().equals(outputHouse.getOwner())){
                throw new IllegalArgumentException("In a transfer the owner has to change");
            }

            //Sign constraints

            Party inputOwner = inputHouse.getOwner();
            Party outputOwner = outputHouse.getOwner();

            if(!(requiredSigners.contains(inputOwner.getOwningKey()))){
                throw new IllegalArgumentException("The current owner must sign the transfer");
            }
            if(!(requiredSigners.contains(outputOwner.getOwningKey()))){
                throw new IllegalArgumentException("The owner must sign the transfer");
            }
        }else{
            throw new IllegalArgumentException("Command Type not recognized");
        }
    }

    public static class Register implements CommandData{

    }

    public static class Transfer implements CommandData{

    }
}
