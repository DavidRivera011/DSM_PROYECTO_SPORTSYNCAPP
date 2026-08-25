
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface CreateUsuarioMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateUsuarioMutation.Data,
      CreateUsuarioMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val nombre: String,
  
    val apellido: String,
  
    val correo: String,
  
    val fechaNacimiento: com.google.firebase.dataconnect.LocalDate,
  
    val rolId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val fotoPerfil: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var nombre: String
        public var apellido: String
        public var correo: String
        public var fechaNacimiento: com.google.firebase.dataconnect.LocalDate
        public var rolId: java.util.UUID
        public var fotoPerfil: String?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          nombre: String,apellido: String,correo: String,fechaNacimiento: com.google.firebase.dataconnect.LocalDate,rolId: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var nombre= nombre
            var apellido= apellido
            var correo= correo
            var fechaNacimiento= fechaNacimiento
            var rolId= rolId
            var fotoPerfil: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var nombre: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { nombre = value_ }
              
            override var apellido: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { apellido = value_ }
              
            override var correo: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { correo = value_ }
              
            override var fechaNacimiento: com.google.firebase.dataconnect.LocalDate
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { fechaNacimiento = value_ }
              
            override var rolId: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { rolId = value_ }
              
            override var fotoPerfil: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { fotoPerfil = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              nombre=nombre,apellido=apellido,correo=correo,fechaNacimiento=fechaNacimiento,rolId=rolId,fotoPerfil=fotoPerfil,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val usuarios_insert: UsuariosKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateUsuario"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateUsuarioMutation.ref(
  
    nombre: String,apellido: String,correo: String,fechaNacimiento: com.google.firebase.dataconnect.LocalDate,rolId: java.util.UUID,

  
    block_: CreateUsuarioMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    CreateUsuarioMutation.Data,
    CreateUsuarioMutation.Variables
  > =
  ref(
    
      CreateUsuarioMutation.Variables.build(
        nombre=nombre,apellido=apellido,correo=correo,fechaNacimiento=fechaNacimiento,rolId=rolId,
  
    block_
      )
    
  )

public suspend fun CreateUsuarioMutation.execute(

  
    
      nombre: String,apellido: String,correo: String,fechaNacimiento: com.google.firebase.dataconnect.LocalDate,rolId: java.util.UUID,

  
    block_: CreateUsuarioMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    CreateUsuarioMutation.Data,
    CreateUsuarioMutation.Variables
  > =
  ref(
    
      nombre=nombre,apellido=apellido,correo=correo,fechaNacimiento=fechaNacimiento,rolId=rolId,
  
    block_
    
  ).execute()


