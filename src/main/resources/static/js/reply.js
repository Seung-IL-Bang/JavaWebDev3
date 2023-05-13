async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`);

    // console.log(result);

    return result.data
}

// 댓글 목록 출력
async function getList({bno, page, size, goLast}) {
    const result = await axios.get(`/replies/list/${bno}`,
        {
            params:{page, size}
        })

    if (goLast) {
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        return getList({bno:bno, page:lastPage, size:size})
    }

    return result.data;
}

// 댓글 등록
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`, replyObj)

    return response.data
}

// 댓글 모달창에서 조회
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)

    return response.data
}

// 댓글 모달창에서 수정
async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)

    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)

    return response.data
}