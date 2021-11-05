/*
export function someMutation (state) {
}
*/
export function setSelectedTab (state, tab) {
    state.selectedTab = tab;
}

export function addPageTab (state, tab) {
    state.singlePageTabs.push(tab);
}

export function deletePageTab (state, tab) {
    let index = state.singlePageTabs.indexOf(tab);
    state.singlePageTabs.splice(index,1);
}