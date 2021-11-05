/*
export function someMutation (state) {
}
*/
export function setApplyModel (state, payload) {
    state.applyModel =payload;
}

export function setFlowLevel (state, newLevel) {
    state.flowLevel = newLevel;
}

export function addContractModel (state, payload) {
    state.mutatedStateList.push(payload);
}