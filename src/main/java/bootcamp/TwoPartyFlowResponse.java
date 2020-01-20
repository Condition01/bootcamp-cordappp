package bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.ServiceHub;

import java.util.List;

@InitiatedBy(TwoPartyFlow.class)
public class TwoPartyFlowResponse extends FlowLogic<Void> {

    private FlowSession counterpartySession;

    public TwoPartyFlowResponse(FlowSession counterpartyFlowSession){
        this.counterpartySession = counterpartyFlowSession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        ServiceHub serviceHub = getServiceHub();

        //recuperar informações do vault, sobre estados que estão guardados lá
        List<StateAndRef<HouseState>> statesFromVault = serviceHub.getVaultService().queryBy(HouseState.class).getStates();

        //recuperar informações de um participante da rede
        CordaX500Name alicesNames = new CordaX500Name("Alice", "Manchester", "UK");
        NodeInfo alice = serviceHub.getNetworkMapCache().getNodeByLegalName(alicesNames);

        //recuperar informações pessoais
        int plataformVersion = serviceHub.getMyInfo().getPlatformVersion();

        int receivedInt = counterpartySession.receive(Integer.class).unwrap(it -> {
            if(it > 3) throw new IllegalArgumentException("Number too high");
            return it;
        });

        int receveidIntPlusOne = receivedInt + 1;

        counterpartySession.send(receivedInt);

        return null;
    }
}
