const API_URL = "http://localhost:8080";
let idOfertaSeleccionada = null;

function entrar() {
    console.log("entrando...");
    listarOfertas();
}

async function listarOfertas() {
    const tableBody = document.querySelector("#body");
    try {
        const response = await fetch(`${API_URL}/api/ofertaeducativa`);
        const datos = await response.json();
        console.log(datos); // Agrega esta línea para ver los datos en la consola

        // Limpiar el cuerpo de la tabla
        tableBody.innerHTML = "";
        // Iterar sobre los datos y agregar filas a la tabla
        datos.forEach((dato) => {
            console.log("Oferta educativa: ", dato.ofertaEducativa);
            console.log("Ocupaciones: ", dato.ocupaciones);
            const row = document.createElement("tr");
            // Agregar columnas a la fila
            const cell1 = document.createElement("td");
            cell1.textContent = dato.ofertaEducativa;
            row.appendChild(cell1);
            const cell2 = document.createElement("td");
            const ul = document.createElement("ul");
            if (dato.ocupaciones && dato.ocupaciones.length > 0) {
                console.log(dato.ocupaciones);
                dato.ocupaciones.forEach((ocu) => {
                    const li = document.createElement("li");
                    li.textContent = ocu.ocupacion;
                    console.log("Ocupaciones: ", ocu.ocupacion);
                    ul.appendChild(li);
                });
            }

            cell2.appendChild(ul);
            row.appendChild(cell2);
            const cell3 = document.createElement("td");
            const btnEditar = document.createElement("button");
            btnEditar.type = "button";
            btnEditar.className = "btn btn-info";
            btnEditar.onclick = function () {
                buscarOferta(dato.id);
            };
            btnEditar.textContent = "Editar";
            const btnEliminar = document.createElement("button");
            btnEliminar.type = "button";
            btnEliminar.className = "btn btn-danger";
            btnEliminar.setAttribute("data-bs-toggle", "modal");
            btnEliminar.setAttribute("data-bs-target", `#eliminarModal${dato.id}`);
            btnEliminar.textContent = "Eliminar";
            cell3.appendChild(btnEditar);
            cell3.appendChild(btnEliminar);
            row.appendChild(cell3);
            // Agregar la fila al cuerpo de la tabla
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error(error);
    }
}

async function enviarFrmAdd() {
    let admi = document.getElementById('nombreAdmision').value;
    let act = document.getElementById('activo').checked;
    let id = document.getElementById('idA').value;
    const params = {
        nombreAdmision: admi,
        activo: act,
        id: id
    };
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    };
    try {
        const response = await fetch(`${API_URL}/guardar-admision`, options);
        if (response.ok) {
            //alert(`${admi} guardado correctamente`);
            listarAdmisiones();
            location.href = "/admisiones";
        } else {
            console.log(response);
            alert('Hubo un error, revisa los datos');
        }
    } catch (exception) {
        console.log(exception);
    }
}

function eliminarRegistro(id) {
// Realiza una solicitud al servidor para eliminar el registro con el ID proporcionado
    const options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    };
    fetch(`${API_URL}/admisiones-borrar/${id}`, options)
            .then(response => {
                if (response.ok) {
                    //alert(`Registro eliminado correctamente`);
                    listarAdmisiones();
                    location.href = "/admisiones";
                } else {
                    console.log(response);
                    //alert('Hubo un error al intentar eliminar la admision');
                }
            })
            .catch(error => {
                console.error(error);
            });
}

async function buscarOferta(id) {
    try {
        let response = await fetch(`${API_URL}/modificarOferta-js/${id}`);
        let oferta = await response.json();
        console.log("Oferta educativa en editar", oferta);

        // Almacenar el id de la oferta en la variable global
        idOfertaSeleccionada = oferta.id;

        // Redirigir a la página de modificarOferta con el id
        window.location.href = `/modificarOferta?id=${idOfertaSeleccionada}`;

    } catch (error) {
        console.error(error);
        throw error;
    }
}









