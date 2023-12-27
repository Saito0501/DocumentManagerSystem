window.addEventListener('load', () => {
	const buttons = document.querySelectorAll('.delete');
	for(button of buttons) {
		button.addEventListener('click', (e) => {
			if(!confirm('本当に削除してよろしいですか？')) {
				e.preventDefault();
			}
		});
	}
});