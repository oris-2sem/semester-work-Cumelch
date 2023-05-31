const queryString = window.location.href;
const parameters = new URLSearchParams(queryString);
let csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
let csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

let pageNo = parameters.get('pageNo')
if (pageNo == null) {
    pageNo = 0;
}
let pageSize = parameters.get('pageSize')
if (pageSize == null) {
    pageSize = 8;
}
let sortBy = parameters.get('sortBy')
if (sortBy == null) {
    sortBy = 'id';
}
let sortDir = parameters.get('sortDir')
if (sortDir == null) {
    sortDir = 'asc';
}
let currentData = undefined;
let tableBody = document.getElementById("tableBody");
let paginationNav = document.getElementById("paginationNav");

fetch(`http://localhost:8080/api/offers/approve?pageSize=${pageSize}&pageNo=${pageNo}&sortBy=${sortBy}&sortDir=${sortDir}`, {
    headers: {
        "Accept": "application/json"
    }
}).then(res => res.json())
    .then(data => {
        currentData = data;

        if (data.content.length == 0) {
            tableBody.innerHTML += `
<th colspan="5" class="text-center justify-content-center text-danger">
<h3>No more offers found!</h3>
</th>

`
        }else{
            for (let offer of data.content) {
                tableBody.innerHTML += `
              <tr>
                <td>
                  <img width="50px" height="50px" src="${offer.imagesUrls[0]}" alt="">
                  <a href="/offers/${offer.id}/details" class="user-link">${offer.title}</a>
                  <span class="user-subhead">Category: ${offer.category}</span>
                </td>
                <td><h6>$${parseFloat(offer.price).toFixed(2)}</h6></td>
                <td class="text-center">
                <h6>
                ${offer.breed}
                </h6>
                </td>
                <td>
                  <a href="/users/profile/${offer.sellerUsername}"><h6>${offer.sellerUsername}</h6></a>
                </td>
                <td style="width: 25%;">
                  <a href="/offers/${offer.id}/details" class="table-link btn btn-warning text-white">
                                         Details
                  </a>
                  <a onclick="onPatch(event,${offer.id})" class="table-link btn btn-success">
                                         Approve
                  </a>
                  <a
                  onclick="onDelete(event,${offer.id})"
                  class="table-link btn btn-danger text-white">
                                          Deny
                  </a>
                </td>
              </tr>
            `;
            }

        }
    })
    .then(() => {
        paginationNav.innerHTML +=
            pageNavFirstLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        paginationNav.innerHTML +=
            pageNavPreviousLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        paginationNav.innerHTML +=
            pageNavNextLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        paginationNav.innerHTML +=
            pageNavLastLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
    })
    .catch(err => console.log(err))

function pageNavFirstLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.pageNo != 0) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/offers/approve?pageSize=${pageSize}&pageNo=${0}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">First</a>
        \n`;
    }else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">First</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}
function pageNavPreviousLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.pageNo > 0) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/offers/approve?pageSize=${pageSize}&pageNo=${parseInt(pageNo)-1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Previous</a>
        \n`;
    }else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Previous</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}

function pageNavNextLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (!currentData.last) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/offers/approve?pageSize=${pageSize}&pageNo=${parseInt(pageNo)+1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Next</a>
        \n`;
    }else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Next</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}

function pageNavLastLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.totalElements != 0) {
        if (currentData.pageNo != currentData.totalPages-1) {
            commentHtml += `<li class="page-item">\n`;
            commentHtml+=`
        <a href="/admin/offers/approve?pageSize=${pageSize}&pageNo=${currentData.totalPages-1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Last</a>
        \n`;
        }
        else{
            commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
            commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Last</a>
        \n`;
        }

    } else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`;
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Last</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}

async function onDelete(event, offerId) {
    if (confirm("Are you sure you want to delete this offer?")) {
        const http = new DeleteHTTP;
        await http.delete(`http://localhost:8080/offers/${offerId}/delete`)
            .then(data => console.log(data))
            .catch(err => console.log(err));

        sleep(2000)
        window.location.reload()
    } else {
        event.preventDefault()
    }

}
async function onPatch(event,offerId) {
    if (confirm("Are you sure you want to approve this offer?")) {
        const http = new PatchHTTP();
        await http.patch(`http://localhost:8080/offers/${offerId}/approve`)
            .then(data => console.log(data))
            .catch(err => console.log(err));
        sleep(2000)
        window.location.reload()
    }else{
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
                [csrfHeaderName]: csrfHeaderValue
            }
        });
        const resData = 'resource deleted...';
        return resData;
    }


}

class PatchHTTP {
    async patch(url) {
        const response = await fetch(url, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeaderName]: csrfHeaderValue
            }
        });
        const resData = 'resource deleted...';
        return resData;
    }


}
function sleep(milliseconds) {
    const start = Date.now();
    while (Date.now() - start < milliseconds);
}