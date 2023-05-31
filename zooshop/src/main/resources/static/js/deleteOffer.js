let deleteBtn = document.querySelectorAll(".deleteBtn");
console.log(deleteBtn.length);
let csrfHeaderNameDeleteOffer = document.head.querySelector('[name=_csrf_header]').content
let csrfHeaderValueDeleteOffer = document.head.querySelector('[name=_csrf]').content
deleteBtn.forEach(b=>b.addEventListener("click", onDelete));

async function onDelete(event) {
    let offerId = event.currentTarget.getAttribute("data-id");
    if (confirm("Are you sure you want to delete this offer?")) {
        const http = new DeleteHTTP;
        await http.delete(`http://localhost:8080/offers/${offerId}/delete`)
            .then(() => console.log("Deleted!"))
            .catch(err => console.log(err));
        window.location.replace("http://localhost:8080/");
    } else {
        event.preventDefault()
    }

}

class DeleteHTTP {
    async delete(url) {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeaderNameDeleteOffer]: csrfHeaderValueDeleteOffer
            }
        });
        const resData = 'resource deleted...';
        return resData;
    }
}