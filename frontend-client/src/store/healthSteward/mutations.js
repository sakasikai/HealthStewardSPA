/*
export function someMutation (state) {
}
*/
export function setApplyModel (state, clientName, newLevel) {
    state.applyModel = {
        applyClient: clientName,
        applyState: newLevel,
    };
}

export function setFlowLevel (state, newLevel) {
    state.flowLevel = newLevel;
}

export function setContractState (state, myState) {
    state.contractState = myState;
}