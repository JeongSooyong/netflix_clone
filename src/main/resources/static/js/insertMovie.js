function openActorSelection(event) {

    event.preventDefault();

    // 팝업 창으로 띄울 URL을 selectionUrl에 할당
    const selectionUrl = '/selectAllActor';

    // 팝업창의 이름을 selectionUrlName 에 할당
    const selectionUrlName = 'selectAllActor';

    // 팝업 창의 속성
    const selectionUrlFeatures = 'width=800, height=600, scrollbars=yes, resizable=yes';

    // 메서드로 팝업창을 연다.
    window.open(selectionUrl, selectionUrlName, selectionUrlFeatures);


}