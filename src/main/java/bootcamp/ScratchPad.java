package bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.List;

public class ScratchPad {
    public static void main(String[] args) {
        StateAndRef<ContractState> inputState = null;
        HouseState outputState = new HouseState("123 Minha Casa", null);
        PublicKey requiredSigner = outputState.getOwner().getOwningKey();
        List<PublicKey> requiredSigners = ImmutableList.of(requiredSigner);
        Party notary = null;

        TransactionBuilder builder = new TransactionBuilder();

        builder.setNotary(notary );

        builder
                .addInputState(inputState)
                .addOutputState(outputState, "java_bootcamp.HouseContract")
                .addCommand(new HouseContract.Register(), requiredSigners);
//        builder.verify();

                //everyone would use the version of the contract provided
        //by the transactionBuilder , and what happens if that contract is actually a
        //fake dummy contract, is invalid

        //people dont have to trust the builder to provide valid contracts
    }
}
