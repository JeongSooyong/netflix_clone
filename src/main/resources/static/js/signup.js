let isIdAvailable = false; // 아이디 사용 가능 여부
let isPasswordMatch = false; // 비밀번호 일치 여부
let isDuplicateCheckPerformed = false; // 중복 확인 버튼 클릭 여부

$(document).ready(function() {

    // 폼 제출 이벤트 가로채기
    $('#signupForm').on('submit', function(event) {
        // 최종 유효성 검사를 위한 현재 입력값 가져오기
        const userId = $('#userIdInput').val();
        const userEmail = $('#userEmailInput').val();
        const userPw = $('#userPwInput').val();
        const confirmUserPw = $('#confirmUserPwInput').val();

        // 아이디 중복 확인 버튼 클릭 여부 확인
        // 중복확인 버튼이 눌리지 않았거나 초기화된 경우
        if (!isDuplicateCheckPerformed) { 
            alert("회원가입을 위해 아이디 중복 확인을 해주세요.");
            event.preventDefault();
            return;
        }

        // 모든 필수 정보가 입력되었는지 확인
        if (!userId || !userEmail || !userPw || !confirmUserPw) {
            alert("회원가입에 필요한 모든 정보를 입력해주세요.");
            event.preventDefault();
            return;
        }

        // 아이디 중복 확인 결과 여부 확인
        if (!isIdAvailable) {
            alert("입력하신 아이디는 사용 불가능합니다. 다른 아이디를 사용하거나 중복 확인을 다시 해주세요.");
            event.preventDefault(); 
            return;
        }

        // 비밀번호 일치 여부 확인
        if (userPw !== confirmUserPw) {
            alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            event.preventDefault(); 
            return;
        }
    });

    $('#userIdInput').on('input', function() {
        isIdAvailable = false;
        isDuplicateCheckPerformed = false; 
        $('#duplicateCheckMsg').text('').css('color', 'black');
        checkFormValidity();
    });

    $('#checkDuplicateBtn').on('click', function() {
        const userId = $('#userIdInput').val();

        if (userId.length < 4) {
            $('#duplicateCheckMsg').text('아이디는 4자 이상이어야 합니다.').css('color', 'orange');
            isIdAvailable = false;
            isDuplicateCheckPerformed = true;
            checkFormValidity();
            return;
        }

        isDuplicateCheckPerformed = true;

        $.ajax({
            url: '/checkUserId',
            type: 'GET',
            data: { userId: userId },
            success: function(response) {
                if (response.isDuplicated) {
                    $('#duplicateCheckMsg').text('이미 사용 중인 아이디입니다.').css('color', 'red');
                    isIdAvailable = false;
                } else {
                    $('#duplicateCheckMsg').text('사용 가능한 아이디입니다!').css('color', 'green');
                    isIdAvailable = true;
                }
                checkFormValidity();
            },
            error: function(xhr, status, error) {
                console.error("AJAX Error: ", status, error, xhr.responseText);
                $('#duplicateCheckMsg').text('아이디 중복 확인 중 오류가 발생했습니다.').css('color', 'gray');
                isIdAvailable = false; // 오류 발생 시 안전하게 '사용 불가능'으로 처리
                // isDuplicateCheckPerformed는 이미 true로 설정되어 있으므로 변경할 필요 없음
                checkFormValidity();
            }
        });
    });

    // 비밀번호 입력 필드와 비밀번호 확인 입력 필드의 내용이 변경될 때
    $('#userPwInput, #confirmUserPwInput').on('input', function() {
        const password = $('#userPwInput').val();
        const confirmPassword = $('#confirmUserPwInput').val();

        if (password === "" && confirmPassword === "") {
            $('#passwordMatchMsg').text('').css('color', 'black');
            isPasswordMatch = false;
        } else if (password === confirmPassword) {
            $('#passwordMatchMsg').text('비밀번호가 일치합니다.').css('color', 'green');
            isPasswordMatch = true;
        } else {
            $('#passwordMatchMsg').text('비밀번호가 일치하지 않습니다.').css('color', 'red');
            isPasswordMatch = false;
        }
        checkFormValidity();
    });

    // 폼 전체의 유효성을 검사하고 회원가입 버튼을 활성화/비활성화하는 함수
    function checkFormValidity() {
        // 모든 필수 입력 필드가 채워져 있는지 확인
        const isFormFilled = $('#userIdInput').val().length > 0 &&
                             $('#userEmailInput').val().length > 0 &&
                             $('#userPwInput').val().length > 0 &&
                             $('#confirmUserPwInput').val().length > 0;
        
        // 아이디 중복 확인이 수행되었고 (isDuplicateCheckPerformed),
        // 아이디가 '사용 가능'으로 확인되었고 (isIdAvailable),
        // 비밀번호가 일치하며 (isPasswordMatch),
        // 모든 필수 입력 필드가 채워져 있다면 (isFormFilled)
        if (isDuplicateCheckPerformed && isIdAvailable && isPasswordMatch && isFormFilled) {
            $('#signupSubmitBtn').prop('disabled', false); // 회원가입 버튼 활성화
        } else {
            $('#signupSubmitBtn').prop('disabled', true); // 그 외의 경우 비활성화
        }
    }

    // 'signupForm' 내의 모든 <input> 필드에 'input' 이벤트 리스너를 추가
    $('#signupForm input').on('input', checkFormValidity);

    // 페이지가 완전히 로드되었을 때 초기 폼 유효성 검사를 한 번 실행
    checkFormValidity();
});