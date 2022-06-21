"use strict"

Vue.createApp({
    data() {
        return {
            datos: [],
            nombre: "",
            apellido: "",
            email: "",
            nombreEditado: "",
            apellidoEditado: "",
            emailEditado: "",
            clienteCapturado: "",
            clienteCapturadoURL: "",
            isActive: false,
        }
    },

    created() {
        axios.get("http://localhost:8080/api/clients")
            .then(res => {
                this.datos = res.data
                let loader = document.querySelector(".loader")
                if (loader) {
                    loader.classList.add("shown")
                }
            })
    },

    methods: {
        addClient() {
            axios.post("http://localhost:8080/rest/clients", {
                firstName: this.nombre,
                lastName: this.apellido,
                email: this.email,
            }).then(res => res)
        },

        getClient(clients){
            this.clienteCapturado = clients
            this.clienteCapturadoURL = clients._links.client.href
        },

        modifyClient(url) {
            this.nombreEditado = document.querySelector("#nameEdit").value
            this.apellidoEditado = document.querySelector("#lastnameEdit").value
            this.emailEditado = document.querySelector("#emailEdit").value

            axios.patch(url, {
                firstName: this.nombreEditado,
                lastName: this.apellidoEditado,
                email: this.emailEditado,
            }).then(location.reload())
        },

        signOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "http://localhost:8080/web/index.html")
        },

        deleteClient(params) {
            axios.delete(params)
                .then(location.reload())
        },
    },

    computed: {
    },

}).mount('#app')