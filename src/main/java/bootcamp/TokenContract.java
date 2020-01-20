package bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        //shape rules
        if(tx.getInputStates().size() != 0 )
            throw new IllegalArgumentException("The number of inputs must be 0");

        if(tx.getOutputStates().size() != 1)
            throw new IllegalArgumentException("The number of outputs must be 1");

        if(tx.getCommands().size() != 1)
            throw new IllegalArgumentException("The number of commands must be 1");
        //content rules
        ContractState output = tx.getOutput(0);
        if(!(output instanceof TokenState))
            throw new IllegalArgumentException("The output need to be a token state");

        TokenState tokenState = (TokenState) output;

        if(tokenState.getAmount() <= 0)
            throw new IllegalArgumentException("The amount in the transaction need to be grater than 0");


        CommandData possibleIssuerCommand = tx.getCommand(0).getValue();
        if(!(possibleIssuerCommand instanceof Commands.Issue))
            throw new IllegalArgumentException("The command must be an issuer command");
        //signer rules
        Party issuer = tokenState.getIssuer();
        if(!(tx.getCommand(0).getSigners().contains(issuer.getOwningKey()))){
            throw new IllegalArgumentException("The command must be an issuer");
        }
    }

    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}