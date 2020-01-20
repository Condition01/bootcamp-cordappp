package bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;

@InitiatingFlow
@StartableByRPC
public class TwoPartyFlow extends FlowLogic<Integer> {

    private Party counterparty;
    private Integer number;


    public TwoPartyFlow(Party conterparty, Integer number){
        this.counterparty = conterparty;
        this.number = number;
    }

    @Suspendable
    @Override
    public Integer call() throws FlowException {
        FlowSession session = initiateFlow(this.counterparty);
        session.send(number);

        int receivedIncrementedInteger = session.receive(Integer.class).unwrap(it -> it);
        return receivedIncrementedInteger;
    }
}
