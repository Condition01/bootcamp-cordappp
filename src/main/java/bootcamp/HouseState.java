package bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HouseState implements ContractState {
    private String adress;
    private Party owner;

    public String getAdress() {
        return adress;
    }

    public Party getOwner() {
        return owner;
    }

    public HouseState(String adress, Party owner) {
        this.adress = adress;
        this.owner = owner;
    }


    public static void main(String[] args) {
        Party joel = null;
        HouseState state = new HouseState("Rua Roberto Rolim de Moura, nº46", joel);
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner);
    }
}
